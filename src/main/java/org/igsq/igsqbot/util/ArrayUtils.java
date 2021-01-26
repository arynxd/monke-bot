package org.igsq.igsqbot.util;

import java.util.List;

public class ArrayUtils
{
	private ArrayUtils()
	{
		//Overrides the default, public, constructor
	}

	public static String arrayCompile(List<String> array, String delimiter)
	{
		StringBuilder builder = new StringBuilder();
		for(String selectedPart : array)
		{
			builder.append(selectedPart).append(delimiter);
		}
		return builder.toString().strip();
	}

	public static boolean isValueInArray(Object[] array, Object value)
	{
		if(array.length == 0) return false;
		for(Object currentObject : array)
		{
			if(currentObject.equals(value))
			{
				return true;
			}
		}
		return false;
	}

	public static boolean isValueInArray(List<Object> array, Object value)
	{
		if(array.isEmpty()) return false;
		for(Object currentObject : array)
		{
			if(currentObject.equals(value))
			{
				return true;
			}
		}
		return false;
	}
}
