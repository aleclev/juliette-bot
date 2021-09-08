package Discord;

public enum ArgumentType {
    INT {
        @Override
        public String toString() {
            return "Whole number";
        }
    },
    FLOAT {
        @Override
        public String toString() {
            return "Real number";
        }
    },
    STRING {
        @Override
        public String toString() {
            return "String of text";
        }
    },
    USER {
        @Override
        public String toString() {
            return "User (mention or id)";
        }
    },
    CHANNEL {
        @Override
        public String toString() {
            return "Channel (mention or id)";
        }
    }
}
