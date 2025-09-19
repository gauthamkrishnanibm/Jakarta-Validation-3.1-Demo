/*******************************************************************************
 * Copyright (c) 2025 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package io.openliberty.jakarta.validation.v31demo.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

/**
 * EmailAddress record demonstrating email validation on a Java record.
 */
public record EmailAddress(@Email @Size(min = 3, max = 100) String value) {
    // No additional methods needed
}
