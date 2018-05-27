package net.brinkervii.quetzalcoatl.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.brinkervii.common.BucketOfShame;

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.OutputStream;
import java.io.Serializable;

public class ResponseBodyWrapper {
	private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	private final OutputStream outputStream;
	private long size = 0;

	public ResponseBodyWrapper(OutputStream responseBody) {
		this.outputStream = responseBody;
	}

	public long getSize() {
		return size;
	}

	public void string(String s) {
		try {
			outputStream.write(s.getBytes());
			size += s.length();
		} catch (IOException e) {
			BucketOfShame.accept(e);
		}
	}

	public void object(Object object) throws InvalidClassException, JsonProcessingException {
		if (!(object instanceof Serializable)) {
			throw new InvalidClassException(object.getClass().toString());
		}

		string(OBJECT_MAPPER.writeValueAsString(object));
	}

	public void bytes(byte[] bytes) throws IOException {
		this.outputStream.write(bytes);
		this.size += bytes.length;
	}
}
