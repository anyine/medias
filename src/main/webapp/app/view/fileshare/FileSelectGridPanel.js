Ext.ns('app.view.fileshare');
app.view.fileshare.FileSelectGridPanel = Ext.extend(Ext.grid.GridPanel, {
	path : '',
	buttonAlign : 'center',
	initComponent : function() {
		var me = this;
		var store = new Ext.data.Store({
			proxy : new Ext.data.HttpProxy({
				url : "cn/wizool/bank/servlet/DocumentServlet?method=getByPath"
			}),
			reader : new Ext.data.JsonReader({
				root : 'root',
				totalProperty : 'total',
				fields : [ 'id', 'length', 'name', 'last' ]
			})
		});
		me.store = store;
		var sm = new Ext.grid.CheckboxSelectionModel();
		me.sm = sm;
		var textfield = new Ext.form.TextField({
			width : 250,
			listeners : {
				specialkey : function(field, e) {
					if (e.getKey() == Ext.EventObject.ENTER) {
						me.select();
					}
				}
			}
		});
		me.textfield = textfield;
		me.columns = [ sm, new Ext.grid.RowNumberer({
			width : 40,
			header : '序号'
		}), {
			header : '名称',
			dataIndex : 'name',
			width : 180
		}, {
			header : '大小',
			dataIndex : 'length',
			align : 'right',
			width : 80
		}, {
			header : '最后修改时间',
			dataIndex : 'last',
			width : 130
		} ];
		me.tbar = [ {
			xtype : 'label',
			text : '路径:'
		}, textfield, {
			xtype : 'button',
			text : '查看',
			iconCls : 'query',
			handler : me.select,
			scope : me
		} ];
		me.buttons = [ {
			text : '提交',
			handler : me.save,
			scope : me
		}, {
			text : '取消',
			handler : function() {
				me.findParentByType().close();
			}
		} ]
		app.view.fileshare.FileSelectGridPanel.superclass.initComponent.apply(
				this, arguments);
	},
	select : function() {
		var me = this;
		var path = me.textfield.getValue();
		me.store.load({
			params : {
				path : path
			}
		});
	},
	save : function() {
		var me = this;
		var model = this.getSelectionModel().selections.items;
		var count = model.length;
		var names = "";
		for ( var i = 0; i < model.length; i++) {
			names += model[i].get("name") + " ";
		}
		var wait = new app.system.WaitWindow();
		wait.show();
		if (count != 0) {
			Ext.Ajax.request({
				url : 'cn/wizool/bank/servlet/DocumentServlet?method=copy',
				params : {
					names : names,
					path : me.textfield.getValue()
				},
				success : function(response) {
					var text = Ext.decode(response.responseText);
					var flag = text.success;
					if (flag) {
						wait.close();
						me.thisStore.load({
							params : {
								start : 0,
								limit : 25
							}
						});
						me.findParentByType().close();
						Ext.example.msg("提示", "操作成功");
					} else {
						Ext.Msg.alert("提 示", flag.message);
					}
				}
			});
		} else {
			Ext.example.msg('提示', '请选中一项');
		}
	}
});