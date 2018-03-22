package com.neusoft.gbw.cp.conver.v7.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.neusoft.gbw.cp.conver.ParameterValidation;
import com.neusoft.gbw.cp.conver.vo.Query;
import com.neusoft.gbw.cp.conver.vo.Report;

public class ParameterValidationService implements ParameterValidation {
	private List<String> errorList = new ArrayList<String>();

	@Override
	public boolean checkQuery(Query query) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		if (!errorList.isEmpty()) {
			errorList.clear();

		}
		Set<ConstraintViolation<Query>> set = validator.validate(query);
		for (ConstraintViolation<Query> constraintViolation : set) {
			errorList.add("错误:" + constraintViolation.getMessage());
		}
		return set.isEmpty();
	}

	@Override
	public boolean checkReport(Report report) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		if (!errorList.isEmpty()) {
			errorList.clear();

		}
		Set<ConstraintViolation<Report>> set = validator.validate(report);
		for (ConstraintViolation<Report> constraintViolation : set) {
			errorList.add("错误:" + constraintViolation.getMessage());
		}
		return set.isEmpty();
	}

	@Override
	public List<String> getErrorMessage() {
		return this.errorList;
	}

}
