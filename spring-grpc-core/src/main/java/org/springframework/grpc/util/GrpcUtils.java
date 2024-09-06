/*
 * Copyright 2016-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Partial copy from net.devh:grpc-spring-boot-starter.
 */
package org.springframework.grpc.util;

import io.grpc.MethodDescriptor;

/**
 * Utility class that contains methods to extract some information from grpc classes.
 *
 * @author Daniel Theuke (daniel.theuke@heuboe.de)
 * @author Dave Syer
 */
public final class GrpcUtils {

	/**
	 * A constant that defines the scheme of a Unix domain socket address.
	 */
	public static final String DOMAIN_SOCKET_ADDRESS_SCHEME = "unix";

	/**
	 * A constant that defines the scheme prefix of a Unix domain socket address.
	 */
	public static final String DOMAIN_SOCKET_ADDRESS_PREFIX = DOMAIN_SOCKET_ADDRESS_SCHEME + ":";

	/**
	 * A constant that defines that the server should listen to any IPv4 and IPv6 address.
	 */
	public static final String ANY_IP_ADDRESS = "*";

	/**
	 * Extracts the domain socket address specific path from the given full address. The
	 * address must fulfill the requirements as specified by
	 * <a href="https://grpc.github.io/grpc/cpp/md_doc_naming.html">grpc</a>.
	 * @param address The address to extract it from.
	 * @return The extracted domain socket address specific path.
	 * @throws IllegalArgumentException If the given address is not a valid address.
	 */
	public static String extractDomainSocketAddressPath(final String address) {
		if (!address.startsWith(DOMAIN_SOCKET_ADDRESS_PREFIX)) {
			throw new IllegalArgumentException(address + " is not a valid domain socket address.");
		}
		String path = address.substring(DOMAIN_SOCKET_ADDRESS_PREFIX.length());
		if (path.startsWith("//")) {
			path = path.substring(2);
			// We don't check this as there is no reliable way to check that it's an
			// absolute path, especially when Windows adds support for these in the future
			// if (!path.startsWith("/")) {
			// throw new IllegalArgumentException("If the path is prefixed with '//', then
			// the path must be absolute");
			// }
		}
		return path;
	}

	/**
	 * Extracts the service name from the given method.
	 * @param method The method to get the service name from.
	 * @return The extracted service name.
	 * @see MethodDescriptor#extractFullServiceName(String)
	 * @see #extractMethodName(MethodDescriptor)
	 */
	public static String extractServiceName(final MethodDescriptor<?, ?> method) {
		return MethodDescriptor.extractFullServiceName(method.getFullMethodName());
	}

	/**
	 * Extracts the method name from the given method.
	 * @param method The method to get the method name from.
	 * @return The extracted method name.
	 * @see #extractServiceName(MethodDescriptor)
	 */
	public static String extractMethodName(final MethodDescriptor<?, ?> method) {
		// This method is the equivalent of MethodDescriptor.extractFullServiceName
		final String fullMethodName = method.getFullMethodName();
		final int index = fullMethodName.lastIndexOf('/');
		if (index == -1) {
			return fullMethodName;
		}
		return fullMethodName.substring(index + 1);
	}

	private GrpcUtils() {
	}

}
