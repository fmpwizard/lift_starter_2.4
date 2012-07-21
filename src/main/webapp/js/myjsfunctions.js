(function(window, $) {
  window.fmpwizard = {
    views: {
      DynamicFields: function(){
        var self = this;
        self.addFields = function() {
          $('#btnAdd').click(function() {
            var num     = $('.clonedInput').length; // how many "duplicatable" input fields we currently have
            var newNum  = new Number(num + 1);      // the numeric ID of the new input field being added
            // create the new element via clone(), and manipulate it's ID using newNum value
            var newElem = $('#input' + num).clone().attr('id', 'input' + newNum);
            // manipulate the name/id values of the input inside the new element
            newElem.children('textarea').attr('class', 'emailContent').attr('id', 'reminderText' + newNum);
            newElem.children('input').attr('class', 'schedule').attr('id', 'runReminderInDays' + newNum);
            // insert the new element after the last "duplicatable" input field
            $('#input' + num).after(newElem);
            // enable the "remove" button
            $('#btnDel').removeAttr('disabled','');
          });
        };
        self.removeFields = function() {
          $('#btnDel').click(function() {
            var num = $('.clonedInput').length; // how many "duplicatable" input fields we currently have
            $('#input' + num).remove();     // remove the last element
            // enable the "add" button
            $('#btnAdd').removeAttr('disabled','');
            // if only one element remains, disable the "remove" button
            if (num-1 == 1)
              $('#btnDel').attr('disabled','disabled');
          });
        };
        self.collectFormData = function() {
          var formData = new Array();
          $(".emailContent").each(function() {
            formData.push([$(this).val(), $(this).parent().children('input').val()]);
          });
          console.log(formData)
          sendDataToServer(formData);
        };
      },
      SomeOtherNameSpace: function(){
        var self = this;
        self.funcNamehere = function() {
          $("#id").val();
        };
      }
    }
  };
})(this, jQuery);

/* You call functions from your html file like this:

<script type="text/javascript">
  $(document).ready(function() {
    window.dyTable = new window.fmpwizard.views.DynamicFields();
    window.dyTable.addFields();
    window.dyTable.removeFields();
  });
</script>
 */
