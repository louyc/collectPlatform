package com.neusoft.np.arsf.base.bundle;

import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.xml.stream.XMLStreamException;

import org.osgi.framework.Bundle;

import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.BaseContext;
import com.neusoft.np.arsf.base.bundle.BaseContextImpl;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.base.bundle.NPBaseConstant;
import com.neusoft.np.arsf.common.util.NPMessage;
import com.neusoft.np.arsf.common.util.NPXmlStream;

public class NPStartControler {

	private static class NPStartControlerHolsder {
		private static final NPStartControler INSTANCE = new NPStartControler();
	}

	private NPStartControler() {
	}

	public static NPStartControler getInstance() {
		return NPStartControlerHolsder.INSTANCE;
	}

	private BaseContext context = BaseContextImpl.getInstance();

	private CopyOnWriteArraySet<String> startedNeuBundles;

	private void initControler() {
		startedNeuBundles = new CopyOnWriteArraySet<String>();
	}

	/**
	 * 有Thread.sleep方法，需要单独线程执行，避免主线程阻断；
	 * @throws XMLStreamException 
	 */
	public void runControler() {
		initControler();
		boolean allStarted = false;
		while (!allStarted && !Thread.currentThread().isInterrupted()) {
			Set<String> unAnswerBundles = getUnAnswerSet();
			if (unAnswerBundles.size() == 0) {
				allStarted = true;
				break;
			}
			String sendMessage = null;
			try {
				sendMessage = getEventData(unAnswerBundles);
			} catch (XMLStreamException e1) {
				Log.warn("NPStartControler 未启动Bundle ：询问处理失败! ");
				e1.printStackTrace();
				break;
			}
			Log.info("NPStartControler 未启动Bundle ： " + sendMessage);
			ARSFToolkit.sendEvent(NPBaseConstant.EventTopic.START_EVENT_TOPIC, sendMessage);
			try {
				Thread.sleep(NPBaseConstant.Control.ANSWER_WAIT_TIME);
			} catch (InterruptedException e) {
				Log.info("NPStartControler Thread 中断");
				break;
			}
		}

	}

	private Set<String> getUnAnswerSet() {
		Set<String> unAnswer = new TreeSet<String>();
		Set<String> neuBundles = getAllNeusoftBundleSet();
		for (String item : neuBundles) {
			if (!startedNeuBundles.contains(item)) {
				unAnswer.add(item);
			}
		}
		return unAnswer;
	}

	/**
	 * 询问：BUNDLE_LIST
	 * 返回：BUNDLE_ANSWER
	 */
	private String getEventData(Set<String> unAnswerBundles) throws XMLStreamException {
		NPMessage message = new NPMessage();
		StringBuffer buffer = new StringBuffer();
		for (String item : unAnswerBundles) {
			buffer.append(item);
			buffer.append(NPBaseConstant.Control.BUNDLE_SPLIT);
		}
		message.put(NPBaseConstant.Control.BUNDLE_LIST, buffer.toString());
		return NPXmlStream.marshal(message);
	}

	public synchronized void addAnswerNeuBundle(String bundleName) {
		if (startedNeuBundles == null) {
			Log.info("arsf.base中startedNeuBundles初始化未进行");
			initControler();
		}
		startedNeuBundles.add(bundleName);
	}

	protected Set<String> getAllNeusoftBundleSet() {
		Bundle[] bundles = context.getContext().getBundles();
		Set<String> neuBundles = new TreeSet<String>();
		for (Bundle bundle : bundles) {
			String bundleName = bundle.getSymbolicName();
			if (bundleName.startsWith(NPBaseConstant.Control.VERDOR)) {
				if (bundleName.startsWith(NPBaseConstant.Control.VERDOR_ARSF)) {
					continue;
				}
				if (bundleName.endsWith(NPBaseConstant.Control.VERDOR_CORE)) {
					continue;
				}
				if (bundleName.endsWith(NPBaseConstant.Control.VERDOR_COMMON)) {
					continue;
				}
				neuBundles.add(bundleName);
			}
		}
		return neuBundles;
	}

}
