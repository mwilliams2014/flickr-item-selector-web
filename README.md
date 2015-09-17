# Flickr Item Selector Web

This project is an OSGi plugin that extends Liferay Item Selector feature by including a Flickr view that will allow portal users to select images from Flickr when they are creating content, such as Blog Entries, Web Content, and potentially any other portlet that uses Item Selector portlet.

By default, the view will display a list of the 500 most interesting photos from the previous day. Users can also search by any keyword in Flickr to find relevant pictures or images.

The module requires some minimum configuration in order to work with Flickr Web Services. This includes creating a Flickr account and providing the following Flickr API information that can be obtained in https://www.flickr.com/services/api/misc.api_keys.html:

* Flickr API Key
* Flickr Shared Secret

If those values are not set, the Flickr view won't be displayed and a console log will inform.

You can obtain those values from https://www.flickr.com/services/api/misc.api_keys.html

There is some additional configuration in order to filter the images based on the size. Flickr provides different sizes for the same image, and this module allows to set some thresholds to fetch the right image.

* Max Height
* Min Height
* Max Width
* Min Width

If any of the previous values is set to 0 it will mean that there is no limit for that field and any value will be valid.

This configuration, as any other OSGI configuration, can be modified from Liferay Control Panel - Configuration - Configuration Admin. There, you will need to search `Flickr item selector configuration` and you can set the values there.

This Flickr View will be displayed in Liferay whenever Item Selector porltet is invoked with `ImageItemSelectorCriterion` criterion and the desired return types contains `URLItemSelectorReturnType`. This includes Blogs and Web Content in an OOTB installation of Liferay 7.
