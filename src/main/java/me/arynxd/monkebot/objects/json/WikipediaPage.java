package me.arynxd.monkebot.objects.json;

import java.time.OffsetDateTime;
import java.util.Arrays;
import javax.annotation.Nullable;
import net.dv8tion.jda.api.utils.data.DataObject;

public class WikipediaPage
{
	public static final String WIKIPEDIA_API = "https://en.wikipedia.org/api/rest_v1/page/summary/";
	private final DataObject dataObject;

	public WikipediaPage(DataObject dataObject)
	{
		this.dataObject = dataObject;
	}

	public @Nullable PageType getType()
	{
		try
		{
			if(!dataObject.hasKey("type"))
				return null;

			return Arrays.stream(PageType.values())
					.filter(page -> page.getParam().equalsIgnoreCase(dataObject.getString("type")))
					.findFirst()
					.orElse(PageType.ERROR);
		}
		catch(Exception exception)
		{
			return null;
		}
	}

	public @Nullable OffsetDateTime getTimestamp()
	{
		return dataObject.hasKey("timestamp")
				? OffsetDateTime.parse(dataObject.getString("timestamp"))
				: null;
	}

	public @Nullable String getExtract()
	{
		return dataObject.getString("extract", null);
	}

	public @Nullable String getTitle()
	{
		return dataObject.getString("displaytitle", null);
	}

	public @Nullable String getThumbnail()
	{
		return dataObject.hasKey("thumbnail")
				? dataObject.getObject("thumbnail").hasKey("source")
					? dataObject.getObject("thumbnail").getString("source")
					: null
				: null;

	}

	public enum PageType
	{
		DISAMBIGUATION("disambiguation"),
		STANDARD("standard"),
		NOT_FOUND("https://mediawiki.org/wiki/HyperSwitch/errors/not_found"),
		ERROR("");

		private final String param;

		public String getParam()
		{
			return param;
		}

		PageType(String param)
		{
			this.param = param;
		}
	}
}
