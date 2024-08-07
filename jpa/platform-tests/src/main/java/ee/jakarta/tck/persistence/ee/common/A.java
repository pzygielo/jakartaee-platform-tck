/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

/*
 * $Id$
 */

package ee.jakarta.tck.persistence.ee.common;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "AEJB_1X1_BI_BTOB")
public class A implements java.io.Serializable {

	// ===========================================================
	// instance variables

	protected String id;

	protected String name;

	protected int value;

	protected B b;

	// ===========================================================
	// constructors

	public A() {
	}

	public A(String id, String name, int value) {
		this.id = id;
		this.name = name;
		this.value = value;
	}

	public A(String id, String name, int value, B b) {
		this.id = id;
		this.name = name;
		this.value = value;
		this.b = b;
	}

	@Id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Basic
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Basic
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	// ===========================================================
	// relationship fields

	@OneToOne(mappedBy = "a")
	public B getB() {
		return b;
	}

	public void setB(B b) {
		this.b = b;
	}

}
