package com.neusoft.np.arsf.net.rest.domain.resc;

import java.util.Map;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import org.restlet.representation.Representation;

import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMBeanUtils;
import com.neusoft.np.arsf.common.util.NMBeanUtilsException;
import com.neusoft.np.arsf.common.util.NPJsonUtil;
import com.neusoft.np.arsf.net.core.NetEventProtocol;
import com.neusoft.np.arsf.net.core.NetEventType;
import com.neusoft.np.arsf.net.rest.domain.msg.MsgDecodeException;
import com.neusoft.np.arsf.net.rest.domain.msg.SyntMsgDecode;
import com.neusoft.np.arsf.net.rest.domain.vo.SyntVO;
import com.neusoft.np.arsf.net.rest.infra.condition.ConditionPoolException;
import com.neusoft.np.arsf.net.rest.infra.constants.Constants;
import com.neusoft.np.arsf.net.rest.infra.context.ClassContext;

@Path("/synt/")
public class SyntResource {

	private static ClassContext context = ClassContext.getContext();

	@PUT
	@Path("get")
	public String getSyncData(Representation entity) {
		String msg = "";
		try {
			String task = entity.getText();
			if(null ==task)
				return null;
			return getRequest(task);
		} catch (InterruptedException e) {
			Log.error("", e);
			msg = "数据采集超时";
		} catch (Exception e) {
			Log.error("", e);
			msg = e.getMessage();
		}
		return Constants.ERROR_CODE + Constants.CAUSE + msg;
	}

	private String getRequest(String task) throws MsgDecodeException, ConditionPoolException, InterruptedException,
			NMBeanUtilsException {
		// 解析任务时，任务发生变化，添加平台的标识位taskId
		SyntVO vo = SyntMsgDecode.decode(task);
		if (vo.getTaskId() == null) {
			vo = generateTaskId(vo);
		}
		context.getTsp().put(vo.getTaskId(), vo);
		String cTask = generateTask(vo);
		ARSFToolkit.sendEvent(getTopic(vo.getTypeId()), cTask);
		context.getCtp().wait(vo.getTaskId());
		SyntVO revo = context.getTsp().remove(vo.getTaskId()).getData();
		Map<String, String> map = NMBeanUtils.getObjectFieldStr(revo);
		map = removeAutoTaskId(map);
		return NPJsonUtil.jsonValueString(map);
	}

	private static String getTopic(String typeId) {
		return NetEventType.REST_SYNT.name() + "_" + typeId;
	}

	private static String generateTask(SyntVO vo) throws NMBeanUtilsException {
		Map<String, String> map = NMBeanUtils.getObjectFieldStr(vo);
		return NPJsonUtil.jsonValueString(map);
	}

	private static Map<String, String> removeAutoTaskId(Map<String, String> map) {
		String taskId = map.get(NetEventProtocol.SYNT_TASK_ID);
		if (taskId.startsWith(TASK_ID_MARK)) {
			map.remove(NetEventProtocol.SYNT_TASK_ID);
		}
		return map;
	}

	private static String TASK_ID_MARK = "synt_task_";

	public static SyntVO generateTaskId(SyntVO vo) {
		String taskId = TASK_ID_MARK + vo.getRequest().hashCode() + "_" + System.currentTimeMillis();
		vo.setTaskId(taskId);
		return vo;
	}
}
