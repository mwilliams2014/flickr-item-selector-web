;(function() {
	var PATH_FLICKR_ITEM_SELECTOR = Liferay.ThemeDisplay.getPathContext() + '/o/flickr-item-selector-web';

	AUI().applyConfig(
		{
			groups: {
				flickr: {
					base: PATH_FLICKR_ITEM_SELECTOR + '/js/',
					modules: {
						'flickr-item-selector': {
							path: 'flickr_item_selector.js',
							requires: [
								'liferay-item-viewer',
								'liferay-portlet-base'
							]
						}
					},
					root: PATH_FLICKR_ITEM_SELECTOR + '/js/'
				}
			}
		}
	);
})();