<%--
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
--%>

<%@ include file="/init.jsp" %>

<%
PortletURL portletURL = (PortletURL)request.getAttribute("portletURL");
List<FlickrPhoto> flickrPhotos = (List<FlickrPhoto>)request.getAttribute("flickrPhotos");
String itemSelectedEventName = (String)request.getAttribute("itemSelectedEventName");
%>

<div id="<portlet:namespace />flickerImageSelectorWrapper">

	<liferay-ui:search-container
		emptyResultsMessage="no-groups-were-found"
		iteratorURL="<%= portletURL %>"
		total="<%= 60 %>"
	>
		<liferay-ui:search-container-results
			results="<%= flickrPhotos %>"
		/>

		<liferay-ui:search-container-row
			className="com.liferay.flickr.item.selector.web.FlickrPhoto"
			cssClass="col-md-2 col-sm-4 col-xs-6"
			modelVar="flickrPhoto"
		>
			<liferay-ui:search-container-column-text>
				<div class="flickr-image" data-title="<%= flickrPhoto.getTitle() %>" data-url="<%= flickrPhoto.getURL() %>">
					<liferay-frontend:card
						footer="<%= flickrPhoto.getUserName() %>"
						imageUrl="<%= flickrPhoto.getPreviewURL() %>"
						title="<%= flickrPhoto.getTitle() %>"
					/>
				</div>
			</liferay-ui:search-container-column-text>
		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator displayStyle="icon" markupView="lexicon" paginate="<%= false %>" />

		<liferay-ui:search-paginator searchContainer="<%= searchContainer %>" />
	</liferay-ui:search-container>
</div>

<aui:script use="flickr-item-selector">
	new Liferay.FlickrItemSelector(
		{
			closeCaption: 'flickr',
			namespace: '<portlet:namespace/>',
			on: {
				selectedItem: function(event) {
					Liferay.Util.getOpener().Liferay.fire('<%= itemSelectedEventName %>', event);
				}
			}
		}
	);
</aui:script>