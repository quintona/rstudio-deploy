RStudio Deploy
=============================================================================

RStudio is an integrated development environment (IDE) for the 
[R programming language](http://www.r-project.org). Some of its
features include:

- Customizable workbench with all of the tools required to work with R in one
place (console, source, plots, workspace, help, history, etc.).
- Syntax highlighting editor with code completion.
- Execute code directly from the source editor (line, selection, or file).
- Full support for authoring Sweave and TeX documents.
- Runs on all major platforms (Windows, Mac, and Linux) and can also be
run as a server, enabling multiple users to access the RStudio IDE using
a web browser.

For more information on RStudio please visit the 
[project website](http://www.rstudio.com/).

This Deployment
-----------------------------------------------------------------------------

This deployment is based on [palletops](www.palletops.com)

In order to use it: 

* clone this repo
* Install [leiningen](http://github.com/technomancy/leiningen), a build tool for clojure. You can downlaod this with your web browser, curl or wget or your favourite download tool, following the install instructions.
* Configure your AWS details

```
lein pallet add-service aws aws-ec2 "your-aws-key" "your-aws-secret-key"
```

Note that this creates a ~/.pallet/services/aws.clj file with your credentials in it.

* Create a key pair in your AWS console called "PalletOps" and place PalletOps.pem somewhere safe
* Add this to your keychain using the command `ssh-add -K /User/name/.ssh/PalletOps.pem` (replace with the correct path, and update permissions to 700 using chmod)
* Next, fire up your repl using `lein repl` and then enter the following commands:

```
(bootstrap )
(spinup )
```

And that is it! This script will deploy the RStudio server for you. Once it is complete, run `(listnodes )` and then ssh using ubuntu@IP address supplied. A user called ruser is automatically created for you, so just set the password using `sudo passwd ruser`. If you want to add other users then use `sudo useradd -d /home/otheruser -m otheruser` 

Have fun!