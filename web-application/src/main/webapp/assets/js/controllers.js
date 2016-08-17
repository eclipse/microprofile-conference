'use strict';

/* Controllers */

var phonecatApp = angular.module('phonecatApp', []);

phonecatApp.controller('PhoneListCtrl', function($scope, $http) {
  $http.get('phone/list').success(function(data) {
    $scope.phones = data.phone;
  });

  $scope.orderProp = 'age';
});
