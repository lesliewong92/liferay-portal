/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.jenkins.results.parser;

/**
 * @author Leslie Wong
 */
public class BuildStatusEventListener implements EventListener {

    @Override
    public void update(Build build) {
        TopLevelBuild topLevelBuild = getTopLevelBuild();

        String topLevelJobName = topLevelBuild.getJobName();

        String batchName = build.getJobVariant();

        int x = batchName.indexOf("/");

        if (x != -1) {
            batchName = jobVariant.substring(0, x);
        }

        StringBuilder sb = new StringBuilder();

        sb.append(_getSlaveUsageGaugeDeltaMessage(build));
        sb.append("|#top_level_job_name:");
        sb.append(topLevelJobName);
        sb.append(",batch_name:");
        sb.append(batchName);

        DatagramRequestUtil.send(sb.toString());
    }

    private String _getSlaveUsageGaugeDeltaMessage(Build build) {
        StringBuilder sb = new StringBuilder();

        int currentSlaveUsageCount = build.getCurrentSlaveUsageCount();

        int slaveUsageDelta = _currentSlaveUsageCount - currentSlaveUsageCount;

        if (slaveUsageDelta != 0) {
            sb.append("build_slave_usage_value:");

            if (slaveUsageDelta > 0) {
                sb.append("+");
            }
            else {
                sb.append("-");
            }

            sb.append(Math.abs(slaveUsageDelta));
            sb.append("|g");
        }

        return sb.toString();
    }

    private int _currentSlaveUsageCount = 0;

}