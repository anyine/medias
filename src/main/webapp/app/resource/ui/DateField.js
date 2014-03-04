Ext.ux.DateField=function(config)
{
    //be true when reset
    this.isReset=false;
    this.dateTime=config.dateTime;
    Ext.ux.DateField.superclass.constructor.call(this,config);
};
 Ext.extend(Ext.ux.DateField,Ext.form.DateField,  {
     
     /**
     * Clear any invalid styles/messages for this field
     */
    clearInvalid : function(){
        if(!this.rendered || this.preventMark){ // not rendered
            return;
        }
        this.el.removeClass(this.invalidClass);
         this.dateTime.clearInvalid();
         //check the other field for datetime
        
        // this.dateTime.tf.isValid();
    },
    /**
     * Mark this field as invalid
     * @param {String} msg The validation message
     */
    markInvalid : function(msg){
        if(!this.rendered || this.preventMark){ // not rendered
            return;
        }
        this.el.addClass(this.invalidClass);
        msg = msg || this.invalidText;
         this.dateTime.markInvalid(msg);
    }
   
})