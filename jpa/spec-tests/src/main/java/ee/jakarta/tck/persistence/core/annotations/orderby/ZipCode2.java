/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.annotations.orderby;

import jakarta.persistence.Embeddable;

@Embeddable
public class ZipCode2 implements java.io.Serializable {

	protected String zip;

	public ZipCode2() {
	}

	public ZipCode2(String zip) {
		this.zip = zip;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public boolean equals(Object o) {
		ZipCode2 other;
		boolean result = false;

		if (!(o instanceof Address)) {
			return result;
		}
		other = (ZipCode2) o;

		if (this.getZip() == other.getZip()) {
			result = true;
		}

		return result;
	}

	public int hashCode() {
		int myHash;

		myHash = this.getZip().hashCode();

		return myHash;
	}
}
