package nl.amc.biolab.config.tools;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.amc.biolab.config.exceptions.ReaderException;
import nl.amc.biolab.config.exceptions.WriterException;
import nl.amc.biolab.config.manager.ConfigurationManager;

import org.json.simple.JSONObject;

public class ConfigurationWriter {
	private FileLock lock;
	private RandomAccessFile file;
	private FileChannel channel;
	
	public String lockException = "File is locked.";

	public ConfigurationWriter() {}

	private void _getLock() throws WriterException {
		try {
			this.file = new RandomAccessFile(ConfigurationManager.read.getConfigFile(), "rw");
			this.channel = this.file.getChannel();

			try {
				this.lock = this.channel.tryLock();
			} catch (OverlappingFileLockException e) {
				throw new WriterException(lockException);
			}
		} catch (FileNotFoundException e) {
			throw new WriterException("Configuration file was not found at provided location");
		} catch (IOException e) {
			throw new WriterException(e.getMessage());
		}
	}

	private void _releaseResources() throws WriterException {
		try {
			this.lock.release();
			this.lock.close();

			this.channel.close();
			this.file.close();
		} catch (IOException e) {
			throw new WriterException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void write(Object obj_to_write, String... keys) throws WriterException {
		_getLock();

		try {
			JSONObject current_obj = null;

			// Get key we want to write to
			String key_to_write = keys[keys.length - 1];

			// Remove the last key from the array
			List<String> list = new ArrayList<String>(Arrays.asList(keys));
			
			list.remove(keys.length - 1);
			keys = list.toArray(new String[list.size()]);

			// Get the object requested and write data
			current_obj = ConfigurationManager.read.getJSONObjectItem(keys);
			current_obj.put(key_to_write, obj_to_write);

			if (current_obj != null) {
				ConfigurationManager.logger.log("Writing: " + ConfigurationManager.read.getFullFile().toJSONString(), 5);

				try {
					this.file.setLength(0L);
					this.file.write(ConfigurationManager.read.getFullFile().toJSONString().getBytes());
				} catch (IOException e) {
					throw new WriterException(e.getMessage());
				}
			}
		} catch (ReaderException e) {
			throw new WriterException(e.getMessage());
		} finally {
			_releaseResources();
		}
	}
}
