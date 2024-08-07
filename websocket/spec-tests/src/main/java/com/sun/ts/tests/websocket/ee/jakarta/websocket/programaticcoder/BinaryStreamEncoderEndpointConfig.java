/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package com.sun.ts.tests.websocket.ee.jakarta.websocket.programaticcoder;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.sun.ts.tests.websocket.ee.jakarta.websocket.coder.InitDestroyBinaryStreamEncoder;

import jakarta.websocket.Decoder;
import jakarta.websocket.Encoder;
import jakarta.websocket.Extension;
import jakarta.websocket.server.ServerEndpointConfig;

public class BinaryStreamEncoderEndpointConfig implements ServerEndpointConfig {

	@Override
	public Map<String, Object> getUserProperties() {
		return Collections.emptyMap();
	}

	@Override
	public Class<?> getEndpointClass() {
		return WSCBinaryStreamEncoderServer.class;
	}

	@Override
	public String getPath() {
		return "/binarystreamencoder";
	}

	@Override
	public List<String> getSubprotocols() {
		return Collections.emptyList();
	}

	@Override
	public List<Extension> getExtensions() {
		return Collections.emptyList();
	}

	@Override
	public Configurator getConfigurator() {
		return new ServerEndpointConfig.Configurator() {
		};
	}

	@Override
	public List<Class<? extends Encoder>> getEncoders() {
		Class<? extends Encoder> clz = InitDestroyBinaryStreamEncoder.class;
		List<Class<? extends Encoder>> list = new LinkedList<>();
		list.add(clz);
		return list;
	}

	@Override
	public List<Class<? extends Decoder>> getDecoders() {
		return Collections.emptyList();
	}

}
