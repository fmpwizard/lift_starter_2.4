(function() {
  $(document).ready(function() {
    //Just like Lift removes elements with this class name
    $('.clearable').remove();
    var chatMessages = new ChatMessages();

    //
    $(document).on('new-chat-message', function(event, data) {
      $('#messages').append('<li>' + data + '</li>')
    });
  });
})();
