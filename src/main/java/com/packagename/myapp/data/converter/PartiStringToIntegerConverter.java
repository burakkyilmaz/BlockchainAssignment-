package com.packagename.myapp.data.converter;

import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.StringToIntegerConverter;

public class PartiStringToIntegerConverter extends StringToIntegerConverter {

	private static final long serialVersionUID = 1L;

	public PartiStringToIntegerConverter(String errorMessage) {
		super(errorMessage);
	}

	@Override
	public String convertToPresentation(Integer value, ValueContext context) {
		if (value == null) {
			return "";
		}

		return value.toString();
	}

}
