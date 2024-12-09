const admin = require("firebase-admin");
admin.initializeApp();
const {onValueCreated} = require("firebase-functions/v2/database");
const {logger} = require("firebase-functions");

/**
 * Hàm tính khoảng cách giữa hai tọa độ
 * @param {Object} loc1 - Đối tượng chứa latitude và longitude
 * @param {Object} loc2 - Đối tượng chứa latitude và longitude
 * @return {Number} Khoảng cách giữa hai tọa độ
 */
function calculateDistance(loc1, loc2) {
  const R = 6371; // Bán kính Trái Đất (km)
  const dLat = toRadians(loc2.latitude - loc1.latitude);
  const dLon = toRadians(loc2.longitude - loc1.longitude);
  const a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
    Math.cos(toRadians(loc1.latitude)) *
    Math.cos(toRadians(loc2.latitude)) *
    Math.sin(dLon / 2) * Math.sin(dLon / 2);
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  return R * c; // khoảng cách (km)
}

/**
 * Chuyển đổi độ sang radians
 * @param {Number} degrees - Giá trị góc độ
 * @return {Number} Giá trị radians
 */
function toRadians(degrees) {
  return degrees * Math.PI / 180;
}

/**
 * Cloud function để chỉ định tài xế cho đơn hàng mới
 */
exports.assignDriver = onValueCreated(
    "/orders/{orderId}",
    async (event) => {
      const orderData = event.data.val();
      if (orderData.status !== "temporary") return null;
      const deliveryLocation = orderData.deliveryLocation;

      let closestDriver = null;
      let minDistance = Infinity;

      // Lấy danh sách tài xế từ Realtime Database
      const driversSnapshot = await admin.database()
          .ref("/drivers").once("value");
      const drivers = driversSnapshot.val();

      // Tìm tài xế gần nhất với trạng thái "available: true"
      for (const driverId in drivers) {
        if (Object.prototype.hasOwnProperty.call(drivers, driverId)) {
          const driver = drivers[driverId];
          if (driver.available) {
            const distance =
            calculateDistance(deliveryLocation, driver.location);
            if (distance < minDistance) {
              minDistance = distance;
              closestDriver = driverId;
            }
          }
        }
      }

      if (closestDriver) {
        // Cập nhật tài xế vào đơn hàng và gửi thông báo
        await event.data.ref.update({
          driver: closestDriver,
          status: "assigned",
        });

        // await admin.database().ref("/notifications").push({
        //   message: "Bạn đã được chỉ định nhận đơn hàng mới!",
        //   timestamp: new Date().toISOString(),
        //   type: "order_assignment",
        //   userId: closestDriver,
        // });

        await admin.database().ref("/drivers/${closestDriver}").update({
          available: false,
        });

        logger.log("Đã chỉ định đơn hàng cho tài xế ${closestDriver}");
      } else {
        logger.log("Không tìm thấy tài xế nào");
      }
    });
