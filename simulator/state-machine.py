import os
import random
from state import NormalState


class Context:
    def __init__(self):
        self.state = NormalState()

    def run(self):
        self.state.run(self)

    @property
    def state(self):
        return self._state

    @state.setter
    def state(self, value):
        self._state = value


if __name__ == '__main__':
    ctx = Context()
    ctx.run()