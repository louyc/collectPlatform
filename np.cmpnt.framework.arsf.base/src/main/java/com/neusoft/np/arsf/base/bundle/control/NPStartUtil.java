package com.neusoft.np.arsf.base.bundle.control;

import com.neusoft.np.arsf.base.bundle.NPBaseConstant;

public class NPStartUtil {

	public static boolean needStartControl(String bundleName) {
		if (!bundleName.startsWith(NPBaseConstant.Control.VERDOR)) {
			return false;
		}
		if (bundleName.startsWith(NPBaseConstant.Control.VERDOR_ARSF)) {
			return false;
		}
		return true;
	}

}
