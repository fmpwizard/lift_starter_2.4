#Controlling the Raspberry PI's gpio pins using Lift

I'm including the assembly plugin, so you can copy one single jar to your PI
and then run:

    sudo java -jar /path/to/jar

You run it in sudo because by default it will try to run on port 80

Then go to your raspberry's ip address:

    http://192.168.2.109

That page will display some info about the PI, and you will notice a GPIO menu link, click on it
and you will be able to toggle the status of each pin.


##building the app

sbt is included so run:


    ./sbt.sh
    >assembly


then copy the generated jar file to your raspberry pi and start it:

    sudo java -jar /path/to/jar


##Working with the REST API.

The only part that may be tricky to get right is the PUT request to change pin statuses, please use:

To set the pin1 to off, you can use:

```
curl \
  -H "Content-Type: application/json" \
  -XPUT http://fmpwizard.no-ip.org/api/raspberrypi/gpio/pin1 \
  -d '{"status":false}'

```
And to get the current status of the pin, you can use:

```
curl \
  -H "Content-Type: application/json" \
  -XGET http://fmpwizard.no-ip.org/api/raspberrypi/gpio/pin1

```

and you would get:

```
{"status":"true","put-uri":"http://fmpwizard.no-ip.org/api/raspberrypi/gpio/pin1"}
```


Enjoy

  **Diego**



