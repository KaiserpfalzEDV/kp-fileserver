# KP-FILESERVER - A kubernetes based fileserver for serving static files to the internet

> You don't need to be crazy to be my friend ... ok, maybe you do. It's just more fun that way.
>
> -- @blue_eyed_darkness on TikTok

![Maven](https://github.com/Paladins-Inn/kp-fileserver/workflows/CI/badge.svg)

## Abstract
This is a small fileserver and management API for these files. It is expected to be run in kubernetes as part of a 
bigger system.

The server has been part of kp-commons but now moved to a project of its own.


## License
The license for the software is LGPL 3.0 or newer. Parts of the software may be licensed under other licences like MIT
or Apache 2.0 - the files are marked appropriately. 

## Architecture

tl;dr (ok, only the bullshit bingo words):
- Immutable Objects (where frameworks allow)
- Relying heavily on generated code
- 100 % test coverage of human generated code
- Every line of code not written is bug free!

Code test coverage for human generated code should be 100%, machine generated code is considered bug free until proven
wrong. Every line that needs not be written is a bug free line without need to test it. So aim for not writing code.


## Distribution
Currently there is no distribution of this software.


## Note from the author
This software is meant do be perfected not finished.

If someone is interested in getting it faster, we may team up. I'm open for that. But be warned: I want to do it 
_right_. So no short cuts to get faster. And be prepared for some basic discussions about the architecture or software 
design :-).

---
Bensheim, 2023-01-07
