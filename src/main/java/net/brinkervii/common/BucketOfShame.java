package net.brinkervii.common;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BucketOfShame {
	public static void accept(Throwable e) {
		e.printStackTrace();
	}
}
