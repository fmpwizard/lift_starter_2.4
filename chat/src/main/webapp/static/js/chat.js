(function() {
  $(document).ready(function() {
    //Just like Lift removes elements with this class name
    $('.clearable').remove();
    var chatMessages = new ChatMessages();

    //
    $(document).on('new-chat-message', function(event, data) {
      $('#messages').append('<li>' + data + '</li>')
    });

    $(document).on('new-ko-chat', function(event, data) {
      chatMessages.addMessage(data)
    });
  });

  ChatMessages = function() {
      var self = this;

      var messages = ko.observableArray();
      self.addMessage = function(newMessage) {
        messages.push(newMessage);
      }

      try {
        ko.applyBindings({ messages: messages }, document.getElementById('chat-messages')[0]);
      } catch(e) {
        //ignore
      }
    }
})();
