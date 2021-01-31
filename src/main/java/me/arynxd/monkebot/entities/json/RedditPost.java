package me.arynxd.monkebot.entities.json;

import net.dv8tion.jda.api.utils.data.DataObject;

public class RedditPost
{
	private final DataObject dataObject;

	public RedditPost(DataObject dataObject)
	{
		this.dataObject = dataObject;
	}

	public String getSubreddit()
	{
		return dataObject.hasKey("subreddit_name_prefixed")
				? dataObject.getString("subreddit_name_prefixed")
				: null;
	}

	public boolean isMedia()
	{
		return getURL().endsWith(".jpg") || getURL().endsWith(".png") || getURL().endsWith(".gif");
	}

	public String getUpvotes()
	{
		return dataObject.hasKey("ups")
				? dataObject.getString("ups")
				: null;
	}

	public String getDownvotes()
	{
		return dataObject.hasKey("downs")
				? dataObject.getString("downs")
				: null;
	}

	public Boolean isNSFW()
	{
		return dataObject.hasKey("over_18")
				? dataObject.getBoolean("over_18")
				: null;
	}

	public Boolean isSpoiled()
	{
		return dataObject.hasKey("spoiler")
				? dataObject.getBoolean("spoiler")
				: null;
	}

	public String getURL()
	{
		return dataObject.hasKey("url")
				? dataObject.getString("url")
				: null;
	}

	public String getAuthor()
	{
		return dataObject.hasKey("author")
				? "u/" + dataObject.getString("author")
				: null;
	}

	public Boolean isStickied()
	{
		return dataObject.hasKey("stickied")
				? dataObject.getBoolean("stickied")
				: null;
	}

	public Boolean isPinned()
	{
		return dataObject.hasKey("pinned")
				? dataObject.getBoolean("pinned")
				: null;
	}

	public String getTitle()
	{
		return dataObject.hasKey("title")
				? dataObject.getString("title")
				: null;
	}
}
