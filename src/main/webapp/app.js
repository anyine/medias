Ext.BLANK_IMAGE_URL = "ext/res/images/default/s.gif";
Ext.onReady(function() {
	Ext.Ajax.request({
		url : 'cn/wizool/bank/servlet/InitServlet?method=systemRes',
		success : function(result) {
			var obj = Ext.decode(result.responseText);
			if (obj.success) {
				var win = new app.system.InitLoginWindow();
				win.show();
			} else {
				var cookieStr = document.cookie;
				if (cookieStr.indexOf('userName') != -1) {
					if (cookieStr.indexOf('userName=null') == -1) {
						var viewport = new app.system.MainView();
					} else {
						var win = new app.system.Login();
						win.show();
					}
				} else {
					var win = new app.system.Login();
					win.show();
				}
			}
		}
	});
});