package org.igsq.igsqbot.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import net.dv8tion.jda.api.entities.Icon;
import org.igsq.igsqbot.handlers.DatabaseHandler;

public class FileUtils
{
	private FileUtils()
	{
		//Overrides the default, public, constructor
	}

	public static InputStream getResourceFile(String fileName)
	{
		InputStream file;
		try
		{
			file = DatabaseHandler.class.getClassLoader().getResourceAsStream(fileName);
		}
		catch(Exception exception)
		{
			return null;
		}
		return file;
	}

	public static Icon getIcon(String url)
	{
		Icon icon;
		try
		{
			icon = Icon.from(new URL(url).openStream());

		}
		catch(Exception exception)
		{
			return null;
		}
		return icon;
	}

	public static String convertToString(InputStream inputStream)
	{
		InputStreamReader isReader = new InputStreamReader(inputStream);
		BufferedReader reader = new BufferedReader(isReader);

		StringBuilder stringBuilder = new StringBuilder();
		String str;
		try
		{
			while((str = reader.readLine()) != null)
			{
				stringBuilder.append(str);
			}
		}
		catch(Exception exception)
		{
			return "";
		}

		return stringBuilder.toString();
	}
}
