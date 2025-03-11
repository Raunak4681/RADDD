angular.module('radarApp')
    .factory('webSocketService', ['$rootScope', function($rootScope) {
        var stompClient = null;
        var service = {};

        service.connect = function() {
            var socket = new SockJS('/api/ws');
            stompClient = Stomp.over(socket);
            
            stompClient.connect({}, function() {
                console.log('WebSocket Connected');
                
                // Subscribe to radar updates
                stompClient.subscribe('/topic/radar/updates', function(message) {
                    var radar = JSON.parse(message.body);
                    $rootScope.$broadcast('radarUpdate', radar);
                });

                // Subscribe to radar deletions
                stompClient.subscribe('/topic/radar/deletions', function(message) {
                    var radarId = JSON.parse(message.body);
                    $rootScope.$broadcast('radarDelete', radarId);
                });
            }, function(error) {
                console.error('WebSocket connection error:', error);
                // Attempt to reconnect after 5 seconds
                setTimeout(service.connect, 5000);
            });
        };

        service.disconnect = function() {
            if (stompClient !== null) {
                stompClient.disconnect();
                stompClient = null;
                console.log('WebSocket Disconnected');
            }
        };

        return service;
    }]);