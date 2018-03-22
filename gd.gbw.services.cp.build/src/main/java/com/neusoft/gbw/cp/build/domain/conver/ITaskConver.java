package com.neusoft.gbw.cp.build.domain.conver;

import java.util.List;

import com.neusoft.gbw.cp.build.infrastructure.exception.ConverException;
import com.neusoft.gbw.cp.load.data.build.domain.vo.BuildInfo;

public interface ITaskConver {

	public List<BuildInfo> conver(Object obj) throws ConverException;
}
