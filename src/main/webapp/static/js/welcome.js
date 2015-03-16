app.controller('WelcomeCtrl', function($scope) {

  var showMode = 'LOGIN';

  $scope.setShowMode = function(mode) {
      $scope.showMode = mode;  
  }    

  $scope.showMode = showMode;

  $scope.submit = function(newPost) {
    dpd.posts.post({
      message: newPost
    }, function(result, error) {
      $scope.stopPostEditing();
      if (error) {
        if (error.message) {
          alert(error.message);
        } else if (error.errors && error.errors.message) {
          alert("Message " + error.errors.message);
        } else {
          alert("An error occurred");
        }
      } else {
        feed.posts.unshift(result);
        $scope.newPost = '';
        $scope.$apply();
      }
    }); 
  };

  

});