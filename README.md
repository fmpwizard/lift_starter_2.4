## Example showing that fixedRender is called again if  I open a new tab of the same comet actor

sbt is included so run:


    ./sbt.sh
    >container:start

then go to

    http://127.0.0.1:8080


1. Open Firebug and go to the Net tab -> XHR panel to see the comet requests
2. Right click on the menu "Using Knockout.js" and open a new tab (or press commmand + click )
3. Notice that a new comet response was sent to the tab you were already at


## Expected

If I open a new tab, I was hoping that no comet information would come to the tab 1, right now I am
adding logic on the client side to ignore the json data if my array was already initiated.

Thanks

  Diego
