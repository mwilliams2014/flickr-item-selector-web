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

package com.liferay.flickr.item.selector.web;

/**
 * @author Sergio González
 */
public class FlickrPhoto {

	public FlickrPhoto(
		String title, String url, String userName, String previewURL) {

		_title = title;
		_url = url;
		_userName = userName;
		_previewURL = previewURL;
	}

	public String getPreviewURL() {
		return _previewURL;
	}

	public String getTitle() {
		return _title;
	}

	public String getURL() {
		return _url;
	}

	public String getUserName() {
		return _userName;
	}

	private final String _previewURL;
	private final String _title;
	private final String _url;
	private final String _userName;

}