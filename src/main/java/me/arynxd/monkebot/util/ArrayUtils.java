package me.arynxd.monkebot.util;

import java.util.List;

public class ArrayUtils
{
	private ArrayUtils()
	{
		//Overrides the default, public, constructor
	}

	@Deprecated
	public static String arrayCompile(List<String> array, String delimiter)
	{
		StringBuilder builder = new StringBuilder();
		for(String selectedPart : array)
		{
			builder.append(selectedPart).append(delimiter);
		}
		return builder.toString().strip();
	}
}
