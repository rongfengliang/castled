import { SettingSchedule } from "@/app/components/pipeline/PipelineSettingsForm";
import { PipelineSchedule } from "../dtos/PipelineCreateRequestDto";
import { ScheduleTimeUnit } from "../enums/ScheduleType";

const getFrequencySecs = (frequency: number, timeUnit: ScheduleTimeUnit) => {
  if (timeUnit == ScheduleTimeUnit.MINUTES) {
    return frequency * 60;
  }
  if (timeUnit == ScheduleTimeUnit.HOURS) {
    return frequency * 60 * 60;
  }
  if (timeUnit == ScheduleTimeUnit.DAYS) {
    return frequency * 24 * 60 * 60;
  }
  return frequency;
};

const getSettingsSchedule = (schedule?: PipelineSchedule): SettingSchedule => {
  if (schedule === undefined) {
    return {};
  }
  const MINUTES_MULTIPLIER: number = 60;
  const HOURS_MULTIPLIER: number = 3600;
  const DAYS_MULTIPLIER: number = 86400;
  let frequency: number = schedule.frequency!;
  if (frequency / DAYS_MULTIPLIER > 0 && frequency % DAYS_MULTIPLIER === 0) {
    return {
      frequency: frequency / DAYS_MULTIPLIER,
      timeUnit: ScheduleTimeUnit.DAYS,
    };
  }
  if (frequency / HOURS_MULTIPLIER > 0 && frequency % HOURS_MULTIPLIER === 0) {
    return {
      frequency: frequency / HOURS_MULTIPLIER,
      timeUnit: ScheduleTimeUnit.HOURS,
    };
  }
  return {
    frequency: frequency / MINUTES_MULTIPLIER,
    timeUnit: ScheduleTimeUnit.MINUTES,
  };
};

export default { getFrequencySecs, getSettingsSchedule };
