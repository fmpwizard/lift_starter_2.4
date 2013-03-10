(function() {
  $(document).ready(function() {
    //Just like Lift removes elements with this class name
    $('.clearable').remove();
    var chatMessages = new ChatMessages();

    $(document).on('new-ko-chat', function(event, data) {
      chatMessages.addMessage(data)
    });
  });

  ChatMessages = function() {
      var self = this;
      //This holds all our messages
      var messages = ko.observableArray();
      self.addMessage = function(newMessage) {
        //Here we add the new message to our array.
        //ko will automatically update our html as we add more items to this array.
        messages.push(newMessage);
      }

      try {
        ko.applyBindings({ messages: messages }, document.getElementById('messages')[0]);
      } catch(e) {
        //ignore
      }
    }
})();
