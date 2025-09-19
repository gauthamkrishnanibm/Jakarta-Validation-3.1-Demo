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

import jakarta.validation.GroupSequence;

/**
 * Interface defining a group sequence to specify the order of validation groups.
 */
@GroupSequence({ FirstGroup.class, SecondGroup.class })
public interface ValidationOrder {
    // Marker interface for validation group sequence
}
