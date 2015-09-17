/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.flickr.item.selector.web.configuration;

import aQute.bnd.annotation.metatype.Meta;

/**
 * @author Sergio González
 */
@Meta.OCD(
	id = "com.liferay.flickr.item.selector.web.configuration.FlickrItemSelectorConfiguration"
)
public interface FlickrItemSelectorConfiguration {

	/**
	 * Set the Flickr Api Key to use Flickr services
	 */
	@Meta.AD(required = false)
	public String apiKey();

	/**
	 * Set the maximum height required to consider valid a Flickr image. If the
	 * image is larger and there is no smaller image it will not be used. A zero
	 * value means no validation for this field.
	 */
	@Meta.AD(deflt = "2000", required = false)
	public int maxHeight();

	/**
	 * Set the maximum width required to consider valid a Flickr image. If the
	 * image is larger and there is no smaller image it will not be used. A zero
	 * value means no validation for this field.
	 */
	@Meta.AD(deflt = "2000", required = false)
	public int maxWidth();

	/**
	 * Set the minimum height required to consider valid a Flickr image. If the
	 * image is smaller and there is no larger image it will not be used. A zero
	 * value means no validation for this field.
	 */
	@Meta.AD(deflt = "0", required = false)
	public int minHeight();

	/**
	 * Set the minimum width required to consider valid a Flickr image. If the
	 * image is smaller and there is no larger image it will not be used. A zero
	 * value means no validation for this field.
	 */
	@Meta.AD(deflt = "0", required = false)
	public int minWidth();

	/**
	 * Set the Flickr Shared Secret to use Flickr services
	 */
	@Meta.AD(required = false)
	public String sharedSecret();

}