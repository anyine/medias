Ext.ns('app.view.fileshare');
app.view.fileshare.ModifyFileWindow = Ext.extend(Ext.Window, {
	title : '修改状态',
	iconCls : 'modify',
	width : 320,
	height : 130,
	buttonAlign : 'center',
	modal : true,
	initComponent : function() {
		var me = this;
		var typeSt = new Ext.data.SimpleStore({
			fields : [ 'id', 'name' ],
			data : [ [ '正常', '正常' ], [ '暂停', '暂停' ], [ '取消', '取消' ] ]
		});
		me.items = [ {
			xtype : 'form',
			bodyStyle : 'padding: 20px; background-color: #DFE8F6',
			items : [ {
				xtype : 'combo',
				fieldLabel : '状态',
				store : typeSt,
				editable : false,
				allowBlank : false,
				triggerAction : 'all',
				valueField : 'name',
				mode : 'local',
				displayField : 'name',
				name : 'type',
				width : 120,
				listeners : {
					select : function(a, b) {
						var s = b.get('name');
						me.findByType('hidden')[0].setValue(s);
					}
				}
			}, {
				xtype : 'hidden',
				name : 'state'
			} ]
		} ]

		me.buttons = [ {
			text : '提交',
			handler : me.save,
			scope : me
		}, {
			text : '取消',
			handler : function() {
				me.close()
			}
		} ];
		app.view.fileshare.ModifyFileWindow.superclass.initComponent.apply(
				this, arguments);
	},
	save : function(button) {
		var me = this;
		var form = me.findByType('form')[0];

		form.getForm().submit({
			url : 'cn/wizool/bank/servlet/DocumentServlet',
			params : {
				method : 'modifyState',
				id : me.thisId
			},
			success : function(form, action) {
				var result = action.result;
				if (result.success) {
					Ext.example.msg("提示", "操作成功");
				}
				me.thisStore.load({
					params : {
						start : 0,
						limit : 25
					}
				});
				me.close();
			},
			failure : function(form, action) {
				console.log(action);
				var result = action.result;
				Ext.Msg.alert("提示", result.message);
			}
		});
	}
});