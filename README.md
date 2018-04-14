# CSC 454 - HW 5B

In this homework you will modify your framework (and possibly fix it, depending on what you did in HW3) to support discrete event simulation, and you will also implement the following network model in it:

The network consists of two machines for working metal. A press flattens small metal balls into disks. It has a bin to hold balls that are waiting to be pressed. A drill puts a hole in the center of each disk, and it also has a bin to hold disks waiting to be drilled. The press flattens a ball in one second, but the drill needs two seconds to do its job. These two machines are connected sequentially: the output from the press goes directly to the input of the drill. The input to the network is coupled with the input to the press, and the output from the drill is coupled with the output from the network.

Both the press and the drill are instances of the same atomic model. Here is its formal description:

A simple state of the model consists of a pair (p, s) representing the number of parts p for the machine to process and the time s remaining to process the first of those parts.

The time advance function is ta((p,s)) = s if p > 0 , and infinity otherwise.

The internal state transition function is deltaint((p, s)) = (p - 1, t), where t is the time the machine needs to process one part (so t = 1 for the press, t = 2 for the drill).

The output function is lambda((p, s)) = 1. A machine always outputs exactly one processed part.

On receiving a number q of parts, the external transition function will place the parts into the bin and begin operating on one of them if the bin was empty. If the machine was already operating on a part, it will still place the new parts in the bin, but will keep operating on the same part afterwards. This is expressed by deltaext((p, s), e, q) =

(p + q, s - e) if p > 0
(p + q, t) if p = 0
When new parts arrive and a part is completed simultaneously, the confluent transition function ejects the completed part, stores the incoming parts, and begins operating on a new part. This is formalized as deltacon((p, s), q) = (p + q - 1, t).

Remember to implement a binary heap (one possible implementation of the priority queue ADT) to use for the event schedule. While we are modeling a network of only two models here, your framework has to support an unlimited number of models / events. Also, your heap must be generic enough to fit in your framework.
