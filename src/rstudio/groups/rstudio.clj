(ns rstudio.groups.rstudio
    "Node defintions for rstudio"
    (:use
     [pallet resource phase]
     [pallet.crate.automated-admin-user :only [automated-admin-user]]
     [pallet.resource.package :as package]
     [pallet.action.exec-script :as a]
     [pallet.compute :as compute]
     [pallet.core :as core]
     [pallet.action.remote-file :as file]
     [pallet.action.user :as user]))
     
(defn bootstrap []
	(require 'pallet.configure))
	
(defn listnodes []
	(compute/nodes (configure/compute-service :aws)))

(def default-node-spec
  (node-spec
   ;:image {:os-family :ubuntu :image-id "us-east-1/ami-3c994355"}
   :image {:os-family :ubuntu :image-id "ap-southeast-2/ami-4cf26376"}
   :hardware {:hardware-id "m1.medium"}     
   :network {:inbound-ports [22 8080 80 8787]} 
   :location {:location-id "ap-southeast-2"}
   ))

(def
  ^{:doc "Defines the type of node rstudio will run on"}
  base-server
  (server-spec
   :phases
   {:bootstrap (phase-fn (automated-admin-user)
   						 (user/user "ruser" 
   						 	:create true 
   						 	:home "/home/ruser"  
   						 	:create-home true))}))

(def
  ^{:doc "Define a server spec for rstudio"}
  rstudio-server
  (server-spec
   :phases
   {:configure (phase-fn
   				 (file/remote-file "/etc/apt/sources.list.d/CRAN.list" :content "deb http://cran.ms.unimelb.edu.au/bin/linux/ubuntu precise/" :overwrite-changes true)
   				 (package/package-manager :update)
                 (package/package "r-base" :y true)
                 (package/package "wget" :y true)
                 (package/package "gdebi-core" :y true)
                 (package/package "libapparmor1" :y true)
                 (a/exec-script "wget http://download2.rstudio.org/rstudio-server-0.97.328-amd64.deb")
                 (a/exec-script "gdebi -n rstudio-server-0.97.328-amd64.deb")
                 )}))

(def
  ^{:doc "Defines a group spec that can be passed to converge or lift."}
  rstudio
  (group-spec
   "rstudio"
   :extends [base-server rstudio-server]
   :node-spec default-node-spec
   ))
   
(defn spinup []
	(core/converge rstudio :compute (configure/compute-service :aws)))
	
(defn pulldown []
	(core/converge (core/group-spec "rstudio" :count 0):compute (configure/compute-service :aws)))
