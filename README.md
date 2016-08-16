## JScheduler

Jscheduler is a simple & easy used for distributed timing task framework. Inspired by tbscheduler & elasticjob, I decide to write
a simple & easy use distributed time task framework. Different to them, JScheduler has central management server for task definition and scheduling. 
You can only define task in Jscheduler server, and client ( application server) is just executing the task. Another, Jschdeuler will not assume the task
type you will execute, it's all depend on you. Jscheduler just invokes a work server in cluster to execute the task at the defined time.

 
##  Feature

* Easy to use
* Central Management
* Support Manual Trigger
* Separate Task Definition and Execution


## Progress

This project is still in test, in the near future I will release beta version.


## Doc

Doc need to add

## License

Copyright 2016  Michael Jiang
 
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
 
    http://www.apache.org/licenses/LICENSE-2.0
 
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

