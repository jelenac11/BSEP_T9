import os
import random
from abc import ABC, abstractmethod
from random import randint
from time import sleep, strftime
from datetime import datetime

FACILITY = ['KERN', 'USER', 'MAIL', 'DAEMON', 'AUTH', 'SYSLOG', 'LPR', 'NEWS',
            'UUCP', 'SOLARISCRON', 'AUTHPRIV', 'FTP', 'NTP', 'CONSOLE', 'SECURITY',
            'CRON', 'LOCAL0', 'LOCAL1', 'LOCAL2', 'LOCAL3', 'LOCAL4', 'LOCAL5', 'LOCAL6', 'LOCAL7']
SEVERITY = ['DEBUG', 'INFORMATIONAL', 'NOTICE', 'WARNING', 'ERROR', 'CRITICAL', 'ALERT', 'EMERGENCY']

IP = ['73.195.255.212', '73.195.255.218', '90.124.124.1', '123.107.19.18', '93.124.25.212']
BLACKLIST_IP = ['13.48.123.212', '128.128.199.200', '14.54.11.127', '22.54.160.93', '43.220.201.254']

USERNAMES = ['username1', 'username2', 'username3', 'username4', 'username5']
RESOURCES = ['index.html', 'home.html', 'patients.html', 'alarms.html']

PATH = 'logging.log'

class State(ABC):
    @abstractmethod
    def run(self, context):
        return NotImplemented


class NormalState(State):
    def run(self, context):
        messages = ['System update', 'System update failed', 'Successful login with username ' + random.choice(USERNAMES), 
        'Unsuccessful login attempt with username ' + random.choice(USERNAMES)]

        now = datetime.now()
        log = now.strftime("%d/%m/%Y-%H:%M:%S") + " " + random.choice(FACILITY) + "_" + SEVERITY[random.randint(0, 2)] + " " + random.choice(IP) + " " + random.choice(messages)

        f = open(PATH, "a")
        f.write(log + "\n")
        f.close()
        sleep(2)

        context.state = get_next_state()
        context.state.run(context)


class BruteForceAttackState(State):
    def run(self, context):
        now = datetime.now()

        username = random.choice(USERNAMES)
        host = random.choice(IP)
        for i in range(5):
            if i == 5:
                log = now.strftime("%d/%m/%Y-%H:%M:%S") + " " + FACILITY[4] + "_" + SEVERITY[3] + " " + host + " " + 'Unsuccessful login attempt with username ' + username
            else:
                log = now.strftime("%d/%m/%Y-%H:%M:%S") + " " + FACILITY[1] + "_" + SEVERITY[1] + " " + host + " " + 'Unsuccessful login attempt with username ' + username
       
            f = open(PATH, "a")
            f.write(log + "\n")
            f.close()
        sleep(2)

        context.state = get_next_state()
        context.state.run(context)


class AccessDeniedState(State):
    def run(self, context):
        messages = ['Access denied to patients view', 'Access denied to logs view', 'Access denied to alarms view']
        now = datetime.now()

        log = now.strftime("%d/%m/%Y-%H:%M:%S") + " " + FACILITY[14] + "_" + SEVERITY[3] + " " + random.choice(IP) + " " + random.choice(messages)

        f = open(PATH, "a")
        f.write(log + "\n")
        f.close()
        sleep(2)

        context.state = get_next_state()
        context.state.run(context)


class BlackListIPConnectionState(State):
    def run(self, context):
        now = datetime.now()

        log = now.strftime("%d/%m/%Y-%H:%M:%S") + " " + FACILITY[14] + "_" + SEVERITY[1] + " " + random.choice(IP) + " " + "Connection from ip address " + random.choice(BLACKLIST_IP)

        f = open(PATH, "a")
        f.write(log + "\n")
        f.close()
        sleep(2)

        context.state = get_next_state()
        context.state.run(context)


class DoSAttackState(State):
    def run(self, context):
        now = datetime.now()

        resource = random.choice(RESOURCES)
        host = random.choice(IP)
        for i in range(20):
            if i >= 15:
                log = now.strftime("%d/%m/%Y-%H:%M:%S") + " " + random.choice(FACILITY) + "_" + SEVERITY[5] + " " + host + " " + 'Requested resource ' + resource
            else:
                log = now.strftime("%d/%m/%Y-%H:%M:%S") + " " + random.choice(FACILITY) + "_" + SEVERITY[1] + " " + host + " " + 'Requested resource ' + resource
       
            f = open(PATH, "a")
            f.write(log + "\n")
            f.close()
        sleep(2)

        context.state = get_next_state()
        context.state.run(context)


class DatabaseErrorState(State):
    def run(self, context):
        now = datetime.now()

        log = now.strftime("%d/%m/%Y-%H:%M:%S") + " " + FACILITY[3] + "_" + SEVERITY[random.choice([4, 6])] + " " + random.choice(IP) + " " + 'Database error'
       
        f = open(PATH, "a")
        f.write(log + "\n")
        f.close()
        sleep(2)

        context.state = get_next_state()
        context.state.run(context)


def get_next_state():
    rnd = random.randint(0, 20)
    if rnd == 1:
        return BruteForceAttackState()
    elif rnd == 2:
        return AccessDeniedState()
    elif rnd == 3:
        return BlackListIPConnectionState()
    elif rnd == 4:
        return DoSAttackState()
    elif rnd == 5:
        return DatabaseErrorState()
    else:
        return NormalState()