package me.arynxd.monkebot.entities.bot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import me.arynxd.monkebot.Monke;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configuration file handler for the {@link me.arynxd.monkebot.Monke bot}
 *
 * @see #getString(ConfigOption)
 */
public class Configuration
{
	public static final File CONFIG_FOLDER = new File("config");
	public static final File CONFIG_FILE = new File(CONFIG_FOLDER, "bot.cfg");
	private static final Logger LOGGER = LoggerFactory.getLogger(Configuration.class);
	private final Monke monke;
	private final List<ConfigurationValue> configValues;

	/**
	 * Constructs a new {@link Configuration configuraton handler}
	 *
	 * @param monke The {@link me.arynxd.monkebot.Monke monke} instance.
	 */
	public Configuration(@Nonnull Monke monke)
	{
		this.monke = monke;
		initFolder();
		initFile();
		this.configValues = loadInitialValues();
	}

	/**
	 * Try to create the 'config/bot.cfg' file, {@code System.exit()} on error.
	 */
	private void initFile()
	{
		try
		{
			if(CONFIG_FILE.createNewFile())
			{
				LOGGER.debug("Created new config file.");
			}
			else
			{
				LOGGER.debug("Config file exists.");
			}
		}
		catch(Exception exception)
		{
			monke.getLogger().error("An exception occurred while creating the config file, abort.", exception);
			System.exit(1);
		}
	}

	/**
	 * Try to create the 'config/' folder, {@code System.exit()} on error.
	 */
	private void initFolder()
	{
		try
		{
			if(CONFIG_FOLDER.mkdir())
			{
				LOGGER.debug("Created new config file.");
			}
			else
			{
				LOGGER.debug("Config file exists.");
			}
		}
		catch(Exception exception)
		{
			monke.getLogger().error("An exception occurred while creating the config folder, abort.", exception);
			System.exit(1);
		}
	}

	/**
	 * Load the initial state of the 'config/bot.cfg' file.
	 *
	 * @return The config values.
	 */
	private List<ConfigurationValue> loadInitialValues()
	{
		List<ConfigurationValue> values = new ArrayList<>();
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE));
			String line;
			while((line = reader.readLine()) != null)
			{
				if(!line.contains("=") || line.startsWith("#"))
				{
					continue;
				}
				String[] elements = line.split("=");
				values.add(new ConfigurationValue(elements[0], elements[1]));
			}
			return applyDefaults(values);
		}
		catch(Exception exception)
		{
			monke.getLogger().error("A config error occurred", exception);
			return Collections.emptyList();
		}
	}

	/**
	 * Apply the default values from {@link ConfigOption options} if the key does not exist.
	 *
	 * @param loadedValues The loaded values to apply the defaults to.
	 * @return The new values
	 */
	private List<ConfigurationValue> applyDefaults(List<ConfigurationValue> loadedValues)
	{
		for(ConfigOption configOption : ConfigOption.values())
		{
			if(loadedValues.stream().map(ConfigurationValue::getKey).noneMatch(key -> configOption.getKey().equals(key)))
			{
				loadedValues.add(new ConfigurationValue(configOption.getKey(), configOption.getDefaultValue()));
			}
		}
		save(loadedValues);
		return Collections.unmodifiableList(loadedValues);
	}

	/**
	 * Save the current config options to file.
	 *
	 * @param configValues The options to save.
	 */
	private void save(List<ConfigurationValue> configValues)
	{
		StringBuilder stringBuilder = new StringBuilder();
		for(ConfigurationValue configurationValue : configValues)
		{
			stringBuilder
					.append(configurationValue.getKey())
					.append("=")
					.append(configurationValue.getValue())
					.append("\n");
		}
		try
		{
			FileWriter fileWriter = new FileWriter(CONFIG_FILE);
			fileWriter.write(stringBuilder.toString());
			fileWriter.flush();
		}
		catch(Exception exception)
		{
			monke.getLogger().error("A config error occurred", exception);
		}
	}

	/**
	 * Gets a {@link ConfigOption option} from the loaded list
	 * <p>This IS a Threadsafe operation.
	 *
	 * @param configOption The config option to load.
	 * @return The retrieved option, or the default.
	 */
	@Nonnull
	public String getString(ConfigOption configOption)
	{
		synchronized(configValues)
		{
			for(ConfigurationValue configurationValue : configValues)
			{
				if(configurationValue.getKey().equals(configOption.getKey()))
				{
					return configurationValue.getValue();
				}
			}
			return configOption.getDefaultValue();
		}
	}

	/**
	 * Represents a key value pair in the {@link me.arynxd.monkebot.Monke bot's} {@link Configuration configuration}.
	 */
	private static class ConfigurationValue
	{
		private String key;
		private String value;

		public ConfigurationValue(String key, String value)
		{
			this.key = key;
			this.value = value;
		}

		public String getKey()
		{
			return key;
		}

		public void setKey(String key)
		{
			this.key = key;
		}

		public String getValue()
		{
			return value;
		}

		public void setValue(String value)
		{
			this.value = value;
		}
	}
}
