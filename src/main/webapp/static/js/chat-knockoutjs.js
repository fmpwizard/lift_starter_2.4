(function() {
  $(document).ready(function() {
    //Just like Lift removes elements with this class name
    $('.clearable').remove();
    var chatMessages = new ChatMessages();

    $(document)
      .on('new-ko-chat', function(event, data) {
        chatMessages.addMessage(data)
    })
      .on('initial-chat-messages', function(event, data){
        //We do this to get the array as one var.
        var messages =  Array.prototype.slice.call(arguments, 1)  ;
        if ( chatMessages.messages().length == 0 ) {
          $.each(messages, function(index, value){
            chatMessages.addMessage( value )
          });

        }
      });
  });

  ChatMessages = function() {
      var self = this;
      //This holds all our messages
      self.messages = ko.observableArray();
      self.addMessage = function(newMessage) {
        //Here we add the new message to our array.
        //ko will automatically update our html as we add more items to this array.
        self.messages.push(newMessage);
      }

      try {
        ko.applyBindings({ messages: self.messages }, document.getElementById('chat-messages')[0]);
      } catch(e) {
        //ignore
      }
    }
})();
