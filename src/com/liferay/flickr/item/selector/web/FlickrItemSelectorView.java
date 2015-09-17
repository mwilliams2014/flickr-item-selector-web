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

import aQute.bnd.annotation.metatype.Configurable;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.interestingness.InterestingnessInterface;
import com.flickr4java.flickr.people.User;
import com.flickr4java.flickr.photos.Extras;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photos.SearchParameters;
import com.flickr4java.flickr.photos.Size;

import com.liferay.flickr.item.selector.web.configuration.FlickrItemSelectorConfiguration;
import com.liferay.item.selector.ItemSelectorReturnType;
import com.liferay.item.selector.ItemSelectorView;
import com.liferay.item.selector.criteria.URLItemSelectorReturnType;
import com.liferay.item.selector.criteria.image.criterion.ImageItemSelectorCriterion;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.portlet.PortletURL;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Sergio González
 */
@Component(
	configurationPid = "com.liferay.flickr.item.selector.web.configuration.FlickrItemSelectorConfiguration",
	immediate = true, service = ItemSelectorView.class
)
public class FlickrItemSelectorView
	implements ItemSelectorView<ImageItemSelectorCriterion> {

	@Override
	public Class<ImageItemSelectorCriterion> getItemSelectorCriterionClass() {
		return ImageItemSelectorCriterion.class;
	}

	public ServletContext getServletContext() {
		return _servletContext;
	}

	@Override
	public List<ItemSelectorReturnType> getSupportedItemSelectorReturnTypes() {
		return _supportedItemSelectorReturnTypes;
	}

	@Override
	public String getTitle(Locale locale) {
		return getLanguageKey(locale, "flickr");
	}

	@Override
	public boolean isShowSearch() {
		return true;
	}

	@Override
	public boolean isVisible(ThemeDisplay themeDisplay) {
		if (Validator.isNull(_flickrItemSelectorConfiguration.apiKey()) ||
			Validator.isNull(_flickrItemSelectorConfiguration.sharedSecret())) {

			_log.error("Please configure Flickr Api Key and Shared Secret");

			return false;
		}

		return true;
	}

	@Override
	public void renderHTML(
			ServletRequest request, ServletResponse response,
			ImageItemSelectorCriterion imageItemSelectorCriterion,
			PortletURL portletURL, String itemSelectedEventName, boolean search)
		throws IOException, ServletException {

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		List<FlickrPhoto> flickrPhotos = new ArrayList<>();

		PhotoList<Photo> photoList = null;

		int delta = GetterUtil.getInteger(
			request.getParameter(SearchContainer.DEFAULT_DELTA_PARAM),
			SearchContainer.DEFAULT_DELTA);
		int cur = GetterUtil.getInteger(
			request.getParameter(SearchContainer.DEFAULT_CUR_PARAM),
			SearchContainer.DEFAULT_CUR);

		Set<String> extras = new HashSet<>();

		extras.add(Extras.OWNER_NAME);

		Flickr flickr = new Flickr(
			_flickrItemSelectorConfiguration.apiKey(),
			_flickrItemSelectorConfiguration.sharedSecret(), new REST());

		try {
			if (search) {
				String keywords = GetterUtil.getString(
					request.getParameter("keywords"));

				photoList = getSearchPhotoList(flickr, keywords, delta, cur);
			}
			else {
				photoList = getInterestingPhotoList(flickr, delta, cur);
			}

			populateFlickrPhotos(
				flickr, photoList, themeDisplay.getLocale(), flickrPhotos);
		}
		catch (FlickrException e) {
			e.printStackTrace();
		}

		request.setAttribute("total", photoList.getTotal());
		request.setAttribute("flickrPhotos", flickrPhotos);
		request.setAttribute("portletURL", portletURL);
		request.setAttribute("itemSelectedEventName", itemSelectedEventName);

		ServletContext servletContext = getServletContext();

		RequestDispatcher requestDispatcher =
			servletContext.getRequestDispatcher("/flickr.jsp");

		requestDispatcher.include(request, response);
	}

	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.flickr.item.selector.web)",
		unbind = "-"
	)
	public void setServletContext(ServletContext servletContext) {
		_servletContext = servletContext;
	}

	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		_flickrItemSelectorConfiguration = Configurable.createConfigurable(
			FlickrItemSelectorConfiguration.class, properties);
	}

	protected PhotoList<Photo> getInterestingPhotoList(
			Flickr flickr, int delta, int cur)
		throws FlickrException {

		InterestingnessInterface interestingnessInterface =
			flickr.getInterestingnessInterface();

		Calendar date = new GregorianCalendar();

		date.add(Calendar.DATE, -1);

		Set<String> extras = new HashSet<>();

		extras.add(Extras.OWNER_NAME);

		return interestingnessInterface.getList(
			date.getTime(), extras, delta, cur);
	}

	protected String getLanguageKey(Locale locale, String key) {
		ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
			"content/Language", locale, getClass());

		return resourceBundle.getString(key);
	}

	protected String getPhotoURL(Collection<Size> sizes) {
		List<Size> sizesList = new ArrayList<>(sizes);

		Iterator<Size> sizeIterator = ListUtil.reverseIterator(sizesList);

		while (sizeIterator.hasNext()) {
			Size size = sizeIterator.next();

			if (isValidSize(size.getHeight(), size.getWidth())) {
				return size.getSource();
			}
		}

		return null;
	}

	protected PhotoList<Photo> getSearchPhotoList(
			Flickr flickr, String keywords, int delta, int cur)
		throws FlickrException {

		PhotosInterface photosInterface = flickr.getPhotosInterface();

		SearchParameters searchParameters = new SearchParameters();

		Set<String> extras = new HashSet<>();

		extras.add(Extras.OWNER_NAME);

		searchParameters.setExtras(extras);
		searchParameters.setText(keywords);

		return photosInterface.search(searchParameters, delta, cur);
	}

	protected boolean isValidSize(int height, int width) {
		if (_flickrItemSelectorConfiguration.minHeight() > 0) {
			if (height < _flickrItemSelectorConfiguration.minHeight()) {
				return false;
			}
		}

		if (_flickrItemSelectorConfiguration.maxHeight() > 0) {
			if (height > _flickrItemSelectorConfiguration.maxHeight()) {
				return false;
			}
		}

		if (_flickrItemSelectorConfiguration.minWidth() > 0) {
			if (width < _flickrItemSelectorConfiguration.minWidth()) {
				return false;
			}
		}

		if (_flickrItemSelectorConfiguration.maxWidth() > 0) {
			if (width > _flickrItemSelectorConfiguration.maxWidth()) {
				return false;
			}
		}

		return true;
	}

	protected void populateFlickrPhotos(
			Flickr flickr, PhotoList<Photo> photoList, Locale locale,
			List<FlickrPhoto> flickrPhotos)
		throws FlickrException {

		PhotosInterface photosInterface = flickr.getPhotosInterface();

		for (Photo photo : photoList) {
			String photoURL = getPhotoURL(
				photosInterface.getSizes(photo.getId()));

			if (Validator.isNull(photoURL)) {
				continue;
			}

			String photoTitle = photo.getTitle();

			if (Validator.isNull(photoTitle)) {
				photoTitle = getLanguageKey(locale, "untitled");
			}

			String userName = null;

			User owner = photo.getOwner();

			if (owner != null) {
				userName = owner.getUsername();
			}

			if (Validator.isNull(userName)) {
				userName = getLanguageKey(locale, "anonymous");
			}

			FlickrPhoto flickrPhoto = new FlickrPhoto(
				photoTitle, photoURL, userName, photo.getSquareLargeUrl());

			flickrPhotos.add(flickrPhoto);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		FlickrItemSelectorView.class);

	private static final List<ItemSelectorReturnType>
		_supportedItemSelectorReturnTypes = Collections.unmodifiableList(
			ListUtil.fromArray(
				new ItemSelectorReturnType[] {
					new URLItemSelectorReturnType()
				}));

	private volatile FlickrItemSelectorConfiguration
		_flickrItemSelectorConfiguration;
	private ServletContext _servletContext;

}