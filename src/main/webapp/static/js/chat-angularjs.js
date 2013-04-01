(function(){
  $(document).ready(function() {
    //Just like Lift removes elements with this class name
    $('.clearable').remove();
    $(document)
      .on('new-chat-message', function(event, data) {
        addNGMessages( data.data )
    })
      .on('initial-chat-messages', function(event, data){
        /**
         * If you open a new tab, Lift will send you all stored messages, so we
         * avoid duplicating them here
         */
        if ( areMessagesLoaded() == false ) {
          $.each(data.data, function(index, value){
            addNGMessages( value )
          });
        }
      });
  });

})();



var app = angular.module("Chat", []);

function getScope() {
  var e = document.getElementById( 'messages' );
  return angular.element( e ).scope();
}
/*function that add messages to our model*/
function addNGMessages( data ) {
  var scope = getScope();
  scope.$apply(function(){
    scope.todos.push( data )
  });
}
/*Do we have the initially loaded messages on this tab?*/
function areMessagesLoaded() {
  var scope = getScope();
  return scope.todos.length > 0
}


/*The model*/
function TodoCtrl( $scope ) {
  $scope.todos = [];

  $(document).on('current-user', function(event, data){
    $scope.profile = {userId : data.userId};
  })

}


app.directive("timeAgo", function($compile) {
  return {
    restrict: "A",
    link: function (scope, element, attr) {
      scope.$watch('todo', function (val) {
        element.find("abbr.time-ago").timeago();
      });
    }
  };
});

app.directive('messageForMe', function() {
  return function(scope, elem, attrs) {
    scope.$watch(attrs.messageForMe, function(value) {
      if(value.toLowerCase().indexOf( scope.profile.userId.toLowerCase() ) > -1  ) {
        elem.addClass('message-to-me');
     }
    });
  };
});

app.filter("timeAgo", function() {
  return function(date) {
    return jQuery.timeago(date);
  };
});

