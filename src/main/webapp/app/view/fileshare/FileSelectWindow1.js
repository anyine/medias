Ext.ns('app.view.fileshare');
app.view.fileshare.FileSelectWindow1 = Ext.extend(Ext.Window, {
	width : 830,
	height : 500,
	title : '选择文件',
	layout : 'fit',
	buttonAlign : 'center',
	modal : true,
	initComponent : function() {
		var me = this;
		var panel = new app.view.fileshare.FileShareGridPanel({
			buttonIsHidden : true,
			title : ''
		});
		me.buttons = [ {
			xtype : 'button',
			text : '保存',
			handler : me.save,
			scope : me
		}, {
			xtype : 'button',
			text : '取消',
			handler : function(button) {
				me.close();
			}
		} ];
		me.items = [ panel ];
		app.view.fileshare.FileSelectWindow1.superclass.initComponent.apply(
				this, arguments);
	},
	save : function(button) {
		var win = button.findParentByType();
		var items = win.items.items[0].getSelectionModel().selections.items;
		var docIds = '';
		for ( var i = 0; i < items.length; i++) {
			docIds += items[i].data.id + ',';
		}
		var wait = new app.system.WaitWindow();
		wait.show();
		Ext.Ajax.request({
			url : 'cn/wizool/bank/servlet/MediaServlet?method=setList',
			params : {
				docIds : docIds,
				type : win.type
			},
			success : function(response) {
				var text = Ext.decode(response.responseText);
				var flag = text.success;
				if (flag) {
					wait.close();
					Ext.example.msg("提示", "操作成功");
					win.close();
				} else {
					Ext.Msg.alert("提 示", "程序异常，请联系管理员！");
				}
			}
		});
	}
});