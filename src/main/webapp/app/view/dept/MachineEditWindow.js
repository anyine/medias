Ext.ns('app.view.dept');
app.view.dept.MachineEditWindow = Ext.extend(Ext.Window, {
	title : '编辑',
	buttonAlign : 'center',
	modal : true,
	initComponent : function() {
		var me = this;
		var typeSt = new Ext.data.SimpleStore({
			fields : [ 'id', 'name' ],
			data : [ [ '广告', '广告' ], [ '培训', '培训' ] ]
		});
		me.items = [ {
			xtype : 'form',
			bodyStyle : 'padding: 20px; background-color: #DFE8F6',
			defaults : {
				labelWidth : 40
			},
			defaultType : 'textfield',
			items : [ {
				xtype : 'textfield',
				fieldLabel : '选择机构',
				value : '点击查看',
				readOnly : true,
				listeners : {
					focus : function() {
						var win = new app.view.dept.BranchCheckWindow();
						win.on('beforehide', function() {
							me.findByType('hidden')[0].setValue(this.id);
							me.findByType('textfield')[0].setValue(this.name);
						});
						win.show();
					}
				}
			}, {
				fieldLabel : '机器名称',
				name : 'name'
			}, {
				fieldLabel : 'IP',
				name : 'ip'
			}, {
				xtype : 'combo',
				fieldLabel : '类型',
				store : typeSt,
				editable : false,
				triggerAction : 'all',
				valueField : 'name',
				mode : 'local',
				displayField : 'name',
				name : 'type',
				width : 130,
				allowBlank : false
			}, {
				fieldLabel : '联系人',
				name : 'linkman'
			}, {
				fieldLabel : '电话',
				name : 'phone'
			}, {
				fieldLabel : '座机',
				name : 'mobilephone'
			}, {
				xtype : 'hidden',
				name : 'pid'
			} ]
		} ];
		me.buttons = [ {
			text : '提交',
			handler : me.save,
			scope : me
		}, {
			text : '取消',
			handler : function() {
				me.close();
			}
		} ]
		app.view.dept.MachineEditWindow.superclass.initComponent.apply(this,
				arguments);
	},
	save : function() {
		var me = this;
		var form = me.findByType('form')[0];
		form.getForm().submit({
			url : 'cn/wizool/bank/servlet/DepartmentServlet',
			params : {
				method : 'editMachine',
				id : me.modelId
			},
			success : function(form, action) {
				var result = action.result;
				me.store.load({
					params : {
						start : 0,
						limit : 25
					}
				});
				if (result.success) {
					Ext.example.msg("提示", "操作成功");
				}
				me.close();
			},
			failure : function(form, action) {
				var result = action.result;
				me.store.load({
					params : {
						start : 0,
						limit : 25
					}
				});
				Ext.Msg.alert("提示", result.message);
			}
		});
	}
});