# Stringbot's Overtone Library

My Overtone synth playground.

## Usage

Probably the most interesting part will be the `fm-ops` module... try this:

    $ lein repl
    sbot-lib.main=> (fm/fm-party 24)
    #<synth-node[loading]: sbot-lib.reverb/schroederverb 42>
    sbot-lib.core=> (stop) ; to clear the reverb synth after it finishes

## License

Eh, whatever. Rip it off and patent it if you must. I'll know you're a bastard and that's what counts.
