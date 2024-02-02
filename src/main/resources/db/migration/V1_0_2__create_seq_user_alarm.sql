-- Drop the existing default value
ALTER TABLE user_alarm
    ALTER COLUMN id DROP DEFAULT;

-- Create user_alarm_id_seq
CREATE SEQUENCE user_alarm_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE user_alarm_id_seq
    OWNER TO sharework;

ALTER SEQUENCE user_alarm_id_seq OWNED BY user_alarm.id;

ALTER TABLE ONLY user_alarm
    ALTER COLUMN id SET DEFAULT nextval('user_alarm_id_seq'::regclass);

-- Alter the sequence to start from 50
ALTER SEQUENCE user_alarm_id_seq RESTART WITH 50;
