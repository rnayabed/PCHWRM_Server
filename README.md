# PCHWRM Server
PC Hardware Resource Monitor For [Raspberry Pi](https://www.raspberrypi.org/), Built with JavaFX

[Screenshots](https://imgur.com/a/4H3YjMH)

Built with JavaFX, Java

**7 inch screen recommended. Minimum 5 inch required to be able to read readings properly**.

## How to Run?

1. Download the latest zip from [releases](https://github.com/dubbadhar/PCHWRM_Server/releases).
2. Run `unzip linux_arm7hf.zip` (Make sure you're in the same director)
3. Then `cd PCHWRM_Server`
4. Run `./run`

## How to change Settings

1. `cd` into the same directory where `linux_arm7hf.zip` was extracted
2. Run `./editConfig`

## Run at Startup

### rc.local method
1. Open `/etc/rc.local` as root.
2. Just before `exit 0` add the following lines 
```
cd <Extracted Directory>
./run
```

### bashrc method
1. Open `~/.bashrc`.
2. Add the following lines at the end of file
```
cd <Extracted Directory>
./run
```

**This method is not recommeneded as `~/.bashrc` runs every time an SSH connection is made to the Pi. You can use Ctrl+C to always quit when a new piHoleLCDStat instance is made.**

## Libraries Used
* [Medusa](https://github.com/HanSolo/Medusa) - Gauges

## License 

[GNU GPL v3](https://github.com/dubbadhar/PCHWRM_Server/blob/master/LICENSE) 


